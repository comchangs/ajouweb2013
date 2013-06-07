package ajou.web.mysearch.controller;

import java.io.UnsupportedEncodingException;

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
		String targetPage = "";
		
		
		try {
			user.setUserId(new String(userId.getBytes("8859_1"),"UTF-8"));
			user.setPassword(new String(password.getBytes("8859_1"),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		
		if(!user.getUserId().isEmpty() && !user.getPassword().isEmpty())
		{
			MySqlConnection sql = new MySqlConnection();
			
			if(sql.selectDb("SELECT user_id FROM user WHERE user_id='" + user.getUserId() + "'") == null)
			{
				sql.insertDB("INSERT user(user_id, password) VALUES ('" + user.getUserId() + "','" + user.getPassword() + "')");
				targetPage = "Login";
			}
			else
				targetPage = "Join";
		}
		else
			targetPage = "Join";
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName(targetPage);
		
		return mv;
	}
	
	@RequestMapping(value = "/JoinForm", method = RequestMethod.POST)
	public ModelAndView JoinForm()
	{
		ModelAndView mv = new ModelAndView();
		String targetPage = "Join";

		mv.setViewName(targetPage);
		
		return mv;
	}
}
