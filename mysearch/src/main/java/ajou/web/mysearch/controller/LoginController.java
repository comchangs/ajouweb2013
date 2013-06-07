package ajou.web.mysearch.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			@RequestParam(value = "password", defaultValue = "") String password,
			HttpServletRequest request,
			HttpServletResponse response){
		
		String targetPage = "Login";
		HttpSession session = request.getSession(true);
		ArrayList<String> userBookmarkList = null;
		
		User user = new User();
		
		try {
			user.setUserId(new String(userId.getBytes("8859_1"),"UTF-8"));
			user.setPassword(new String(password.getBytes("8859_1"),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(!user.getUserId().equals("") && !user.getPassword().equals(""))
		{
			MySqlConnection sql = new MySqlConnection();
			
			if(sql.selectUserPasswod(userId).equals(password))
			{
				targetPage = "SearchResultTest";
				session.setAttribute("user", user);
				userBookmarkList = getUserBookmark(user.getUserId());
			}
		}
		ModelAndView mv = new ModelAndView();

		mv.addObject("userBookmarkList", userBookmarkList);
		mv.setViewName(targetPage);
		
		return mv;
	}
	
	public ArrayList<String> getUserBookmark(String userId)
	{
		MySqlConnection sql = new MySqlConnection();

		ArrayList<String> result = sql.selectDb("SELECT url FROM bookmark WHERE user_id='"+ userId + "'");
		
		if(result == null)
		{
			result = new ArrayList<String>();
			result.add(new String("즐겨찾기 목록이 없습니다."));
		}
		
		return result;
	}
}
