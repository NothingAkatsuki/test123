//確認用

package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.repositories.PersonRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Controller
public class MyController4 {

	@Autowired
	PersonRepository repository;
	
	
	@GetMapping("/")
	public ModelAndView index(@ModelAttribute("formModel") Person Person,
			ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("title", "Hello page");
		mav.addObject("msg", "this is JPA sample data.");
		List<Person> list =repository.findAll();
		mav.addObject("data", list);
		return mav;
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(@ModelAttribute Person Person,
			@PathVariable int id, ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title", "edit Person.");
		Optional<Person> data = repository.findById((long)id);
		mav.addObject("formModel", data.get());
		return mav;
	}
	
	@GetMapping("/delete/{id}")
	public ModelAndView edit(@PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title", "Delete Person.");
		mav.addObject("msg", "Can I delete this record?");
		Optional<Person> data = repository.findById((long)id);
		mav.addObject("formModel", data.get());
		return mav;
	}
	
	@PostMapping("/")
	@Transactional
	public ModelAndView form(@ModelAttribute("formModel") @Validated Person Person,
			BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		System.out.println(result.getFieldErrors());
		if(!result.hasErrors()) {
			repository.saveAndFlush(Person);
			res = new ModelAndView("redirect:/");
		} else {
			mav.setViewName("index");
			mav.addObject("title", "Hello page");
			mav.addObject("msg", "sorry, error is occurred.");
			List<Person> list =repository.findAll();
			mav.addObject("data", list);
			res = mav;
		}
		return res;
	}
	
	@PostMapping("/edit")
	@Transactional
	public ModelAndView upadte(@ModelAttribute("formModel") @Validated Person Person,
			BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		System.out.println(result.getFieldErrors());
		if(!result.hasErrors()) {
			repository.saveAndFlush(Person);
			return new ModelAndView("redirect:/");
		} else {
			mav.setViewName("edit");
			mav.addObject("title", "edit Person.");
			res = mav;
		}
		return res;
	}
	
	@PostMapping("/delete")
	@Transactional
	public ModelAndView remove(@RequestParam long id,
			ModelAndView mav) {
		repository.deleteById(id);
		return new ModelAndView("redirect:/");
	}
	
	@PostConstruct
	public void init() {
		//１つ目のダミーデータ作成
		Person p1 = new Person();
		p1.setName("taro");
		p1.setAge(39);
		p1.setMail("taro@yamada");
		repository.saveAndFlush(p1);
		//２つ目のダミーデータ作成
		Person p2 = new Person();
		p2.setName("hanako");
		p2.setAge(28);
		p2.setMail("hanako@flower");
		repository.saveAndFlush(p2);
		//３つ目のダミーデータ作成
		Person p3 = new Person();
		p3.setName("sachico");
		p3.setAge(17);
		p3.setMail("sachico@happy");
		repository.saveAndFlush(p3);
	}
}
