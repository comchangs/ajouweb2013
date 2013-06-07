package ajou.web.mysearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ajou.web.mysearch.model.MySqlConnection;
import ajou.web.mysearch.model.User;


@Controller
public class LoginController {
	
	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public ModelAndView Join(
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "password", defaultValue = "") String password){
		
		String targetPage = "SearchResultTest";
		
		User user = new User();
		
		user.setUserId(userId);
		user.setPassword(password);
		
		if(!user.getUserId().isEmpty() && !user.getPassword().isEmpty())
		{
			MySqlConnection sql = new MySqlConnection();
			
			if(!sql.selectUserPasswod(userId).equals(password))
				targetPage = "Login";
		}
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("userId", userId);
		mv.setViewName(targetPage);
		
		return mv;
	}
}
