package com.ojas.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ojas.model.QuestionForm;
import com.ojas.model.Result;
import com.ojas.model.User;
import com.ojas.service.QuizService;
import com.ojas.service.UserService;

import java.util.List;

import javax.validation.Valid;

@Controller
public class ViewController {

	@Autowired
	UserService userService;
	@Autowired
	User user;
	@Autowired
	Result result;
	@Autowired
	QuizService qService;
	
	Boolean submitted = false;

	@GetMapping(path = { "/", "/login" })
	public ModelAndView loginPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login.html");
		return modelAndView;
	}

	@GetMapping(path = "/register")
	public ModelAndView registrationPage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register.html");
		return modelAndView;
	}

	@PostMapping(path = "/register")
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, RedirectAttributes ra) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "There is already a user registered with this Email");
		}
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", "error.user", "Password did not matched");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("register.html");

		} else {
			userService.saveUser(user);
			ra.addFlashAttribute("successMessage1", "User has been registered successfully");
			return new ModelAndView("redirect:/login");

		}
		return modelAndView;
	}

	@GetMapping("/home")
	public String homePage() {
	//	ModelAndView modelAndView = new ModelAndView();
	//	modelAndView.setViewName("/index.html");
		return "homeee.html";             //changed to index.html
	}
	
	@GetMapping("/indexx")
	public String homePage1() {
		return "index.html";
	}
	
	@ModelAttribute("result")
	public Result getResult() {
		return result;
	}
	
	
	
	@GetMapping(path = "/homeee")
	public ModelAndView homeeePage() {
		ModelAndView modelAndView = new ModelAndView();    
		modelAndView.setViewName("/homeee.html");
		return modelAndView;
	}
	
	@PostMapping("/quiz")
	public String quiz(@RequestParam String username, Model m, RedirectAttributes ra) {
		if(username.equals("")) {
			ra.addFlashAttribute("warning", "You must enter your name");
			return "redirect:/";                                            //   remember
		}
		
		submitted = false;
		result.setUsername(username);
		
		com.ojas.model.QuestionForm qForm = qService.getQuestions();
		m.addAttribute("qForm", qForm);
		
		return "quiz.html";
	}
	
	@PostMapping("/submit")
	public String submit(@ModelAttribute QuestionForm qForm, Model m) {
		if(!submitted) {
			result.setTotalCorrect(qService.getResult(qForm));
			qService.saveScore(result);
			submitted = true;
		}
		
		return "result.html";
	}
	
	@GetMapping("/score")
	public String score(Model m) {
		List<Result> sList = qService.getTopScore();
		m.addAttribute("sList", sList);
		
		return "scoreboard.html";
	}

}
