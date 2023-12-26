package com.sample_project.AdminApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample_project.AdminApp.dto.CompanyDto;
import com.sample_project.AdminApp.dto.GloQuoraPostDto;
import com.sample_project.AdminApp.dto.QuoraPostDto;
import com.sample_project.AdminApp.dto.UserDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Quora")
public class AdminController {

	Logger logger = LoggerFactory.getLogger(AdminController.class);
	ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private RestTemplate restTemplate;


	//AdminService adminService;
//@GetMapping(value = "/getAll")
//	public String  getAll(){
//		return "YOHO";
//}




	@GetMapping(value = "/getPostByUserId/{id}")
	public String getUserPostById(@PathVariable("id") String userId) {
		logger.info("Inside getUserPostById of AdminController");
		try {
            String result =  new RestTemplate().getForObject("http://localhost:8085/userspost/getPost/" + userId, String.class);
            if(result.equals("")) return "No such user present";
        	return result;
		}
        catch(Exception e){
			System.out.println(e.getMessage());
		}
	return "";
	}



	@GetMapping(value = "/getAllUser")
	public ResponseEntity<List<UserDetailsDto>> getDataFromExternalApi() {
	List<UserDetailsDto> users = restTemplate.getForObject("http://localhost:9001/User/getAll", List.class);

		if (users == null) {
			return (ResponseEntity<List<UserDetailsDto>>) null;
		}
		return new ResponseEntity<List<UserDetailsDto>>(users, HttpStatus.OK);
}
	@GetMapping(value = "/getAllUserPost")
	public ResponseEntity<List<Map<String, List<QuoraPostDto>>>> getallusersPosts(){
	Map<String,List<QuoraPostDto>> result = new HashMap<>();
	List<Map<String,List<QuoraPostDto>>>returnitem=new ArrayList<>();
	List<Map<String, Object>> response = restTemplate.getForObject("http://localhost:9001/User/getAll", List.class);
		if (response != null) {
			for (Map<String, Object> user : response) {
				Integer userId = (Integer) user.get("id");
				String username = (String) user.get("userName");
				List<QuoraPostDto> post = new RestTemplate().getForObject("http://localhost:8085/userspost/getPost/"+userId, List.class);

				// Use the userId as needed
				//System.out.println("User ID: " + userId);

				if(post!=null){
				result.put(username, post);
				returnitem.add(result);}
			}
		}
		return new ResponseEntity<List<Map<String,List<QuoraPostDto>>>>(returnitem, HttpStatus.OK);
	}


	
//	@GetMapping(value = "/getUserPostGReaterThanANumber")
//	public List<UserDetailsDto> getUserPostByCount(@RequestParam Long postCount) {
//		logger.info("Inside getUserPostGReaterThanANumber of AdminController");
//		return new ResponseEntity<Object>(adminService.getUserPostByNumber(postCount), HttpStatus.OK);
//	}
//
	@GetMapping(value = "/getCompanyName")
	public ResponseEntity<List<String >> getCompanyNames() {
		logger.info("Inside getCompanyNames of AdminController");
		List<Map<String, Object>> users = new RestTemplate().getForObject("http://localhost:9001/User/getAll", List.class);
		List<String> companyNames = new ArrayList<String>();
		if (users != null) {
			for (Map<String, Object> o : users) {
				System.out.println("here");
				if (o.containsKey("company") && o.get("company") != null) {
					Map<String, Object> companyMap = (Map<String, Object>) o.get("company");
					String cname = (String) companyMap.get("name");

					companyNames.add(cname);
				}
			}
		}
			return new ResponseEntity<List<String>>(companyNames, HttpStatus.OK);

	}
}
