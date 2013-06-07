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
import ajou.web.mysearch.model.Bookmark;


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
		ArrayList<Bookmark> userBookmarkList = null;
		
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
	
	public ArrayList<Bookmark> getUserBookmark(String userId)
	{
		MySqlConnection sql = new MySqlConnection();

		ArrayList<Bookmark> result = sql.getBookmark("SELECT url, name FROM bookmark WHERE user_id='"+ userId + "'");
		
		return result;
	}
}
