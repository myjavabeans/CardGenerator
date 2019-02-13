package com.anjan.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjan.bean.CardNumberBean;
import com.anjan.cardgen.Base64Decoder;
import com.anjan.cardgen.Mod10Card;

@Controller
public class CardNumberController {

	private static Logger logger = Logger.getLogger(CardNumberController.class);
	
	@RequestMapping(value = {"/default", "/"}, method = RequestMethod.GET)
	public String defaultPage(Model model, Locale locale) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate);
		
		return "index";
	}
	
	@RequestMapping(value="/validate", method = RequestMethod.GET)
	public String validateCardPage(Model model, RedirectAttributes redirectAttributes){
		
		logger.info("Accessing Validate Card page...");
		redirectAttributes.addFlashAttribute("valCard","true");
		return "redirect:/";
		
	}
	
	@RequestMapping(value="/generate", method = RequestMethod.GET)
	public String generateCardPage(Model model, RedirectAttributes redirectAttributes){
		
		logger.info("Accessing Generate Card page...");
		
		redirectAttributes.addFlashAttribute("cardGen","true");
		
		return "redirect:/";
		
	}
	
	@RequestMapping(value="/decode", method = RequestMethod.GET)
	public String decodeImagePage(Model model, RedirectAttributes redirectAttributes){
		
		logger.info("Accessing Decode Image page...");
		
		redirectAttributes.addFlashAttribute("decImg","true");
		
		return "redirect:/";
		
	}
	
	@RequestMapping(value="/validateCard", method =  RequestMethod.POST)
	public String validateCardPage(@Validated CardNumberBean bean, Model model, RedirectAttributes redirectAttributes){
		
		String cardNumber = bean.getBeginRange();
		
		logger.info("Validating Card Number Started...");
		
		CardNumberBean cBean = Mod10Card.validateCardNumber(cardNumber);
		
		logger.info("Validating Card Number End...");
		
		String msg = cBean.getMessage();
		
		redirectAttributes.addFlashAttribute("valCard","true");
		redirectAttributes.addFlashAttribute("msg", msg);
		return "redirect:/";
	}
	
	@RequestMapping(value="/generateCard", method =  RequestMethod.POST)
	public String generateCardPage(@Validated CardNumberBean bean, Model model, RedirectAttributes redirectAttributes){
		
		String beginRange = bean.getBeginRange();
		String endRange = bean.getEndRange();
		
		logger.info("Generating Card Numbers Started...");
		
		CardNumberBean cBean = Mod10Card.getValidCardNumbers(beginRange, endRange);
		
		logger.info("Generating Card Numbers End...");
		
		List<String> cardNumbers = cBean.getCardNumbers();
		
		logger.info("Total Valid Cards generated - "+cardNumbers.size());
		
		String msg = cBean.getMessage();
		
		redirectAttributes.addFlashAttribute("cardGen","true");
		redirectAttributes.addFlashAttribute("msg", msg);
		redirectAttributes.addFlashAttribute("cardNumbers", cardNumbers);
		return "redirect:/";
	}
	
	@RequestMapping(value="/decode", method =  RequestMethod.POST)
	public String decodeImagePage(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
		
		String base64Str = request.getParameter("baseStr");
		String extnType = request.getParameter("extnType");
			
		logger.info("Decoding Base64 String Started...");
		
		CardNumberBean bean = Base64Decoder.decodeImage(base64Str, extnType);
		String fileLink = bean.getFileLink();
		String msg = bean.getMessage();
		
		logger.info("Decoding Base64 String End...");
		
		String isImage = "";
		
		if(extnType != null && (extnType.equalsIgnoreCase("html")) || extnType.equalsIgnoreCase("htm") || extnType.equalsIgnoreCase("txt")){
			isImage = "false";
		}else{
			isImage = "true";
		}
		
		redirectAttributes.addFlashAttribute("decImg","true");
		redirectAttributes.addFlashAttribute("fileLink",fileLink);
		redirectAttributes.addFlashAttribute("isImage",isImage);
		redirectAttributes.addFlashAttribute("msg", msg);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/fileLink", method =  RequestMethod.GET)
	public void getFileLink(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam("loc") String location){
		
		File file = new File(location);
		
		try{
			if(file.exists()){
				logger.info(location+" exists...");
				
				byte[] byteFile = Files.readAllBytes(file.toPath());
				
				boolean flag = file.delete();
				
				if(flag){
					logger.info(file.getName()+" deleted successfully");
				}else{
					logger.warn("Error in Deleting File - "+file.getName());
				}
				
				//response.setContentType("image/jpeg, image/jpg, image/png, image/gif, text/html, text/plain");
		        response.getOutputStream().write(byteFile);
		        response.getOutputStream().close();
			}else{
				logger.info("Download link expired...");
				response.getOutputStream().print("Download link Expired. Try again to Decode.");
				response.getOutputStream().close();
			}
		}catch(Exception e){
			logger.warn("Exception Occurred", e);
		}
		
	}
	
}
