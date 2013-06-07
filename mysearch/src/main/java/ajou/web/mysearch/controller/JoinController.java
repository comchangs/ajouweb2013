package ajou.web.mysearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ajou.web.mysearch.model.MySqlConnection;
import ajou.web.mysearch.model.User;


@Controller
public class JoinController {
	
	@RequestMapping(value = "/Join", method = RequestMethod.POST)
	public ModelAndView Join(
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "password", defaultValue = "") String password){
		
		User user = new User();
		
		user.setUserId(userId);
		user.setPassword(password);
		
		if(!user.getUserId().isEmpty() && !user.getPassword().isEmpty())
		{
			MySqlConnection sql = new MySqlConnection();
			
			if(sql.selectDb("SELECT user_id FROM user WHERE user_id=" + userId).isEmpty())
			{
				sql.insertDB("INSERT user(user_id, password) VALUES (" + userId + "," + password + ")");
			}
		}
		
		
		ModelAndView mv = new ModelAndView();
//		mv.addObject("userId", userId);
//		mv.addObject("password", password);
		
		mv.setViewName("Login");
		
		return mv;
	}
}
