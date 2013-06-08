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
		User user = null;
		
		if((user = (User) session.getAttribute("user")) != null)
		{
			targetPage = "SearchResultTest";
			if(user.getBookmark().size() > 1)
				if(user.getBookmark().get(0).getName().equals("북마크 목록이 없습니다."))
					user.getBookmark().remove(0);
		}
		else{
			user = new User();
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
					user.setBookmark(getUserBookmark(user.getUserId()));
					session.setAttribute("user", user);
				}
			}
		}
		ModelAndView mv = new ModelAndView();

		mv.addObject("userBookmarkList", user.getBookmark());
		mv.setViewName(targetPage);
		
		return mv;
	}
	
	public ArrayList<Bookmark> getUserBookmark(String userId)
	{
		MySqlConnection sql = new MySqlConnection();

		ArrayList<Bookmark> result = sql.getBookmark("SELECT url, name FROM bookmark WHERE user_id='"+ userId + "'");
		if(result.isEmpty())
		{
			Bookmark mark = new Bookmark();
			mark.setName("북마크 목록이 없습니다.");
			result.add(mark);
		}
		else
			for(int i =0; i < result.size(); i++)
				result.get(i).setName(result.get(i).getName() + " :: ");
		
		return result;
	}
}
