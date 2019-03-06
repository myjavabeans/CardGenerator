package com.anjan.controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjan.bean.CardNumberBean;
import com.anjan.bean.Gen3DesBean;
import com.anjan.bo.EmployeeBeanBOImpl;
import com.anjan.bo.IssuerDetailsBOImpl;
import com.anjan.cardgen.Base64Decoder;
import com.anjan.cardgen.Gen3Des;
import com.anjan.cardgen.Mod10Card;

@Controller
public class CardNumberController {

	private static Logger logger = Logger.getLogger(CardNumberController.class);
	
	private EmployeeBeanBOImpl employeeBO;
	private IssuerDetailsBOImpl issuerBO;

	@Autowired(required = true)
	@Qualifier(value = "employeeBo")
	public void setEmployeeBO(EmployeeBeanBOImpl employeeBO) {
		this.employeeBO = employeeBO;
	}

	@Autowired(required = true)
	@Qualifier(value = "issuerBo")
	public void setIssuerBO(IssuerDetailsBOImpl issuerBO) {
		this.issuerBO = issuerBO;
	}

	private static String getHostName(){
		String hostname = "";
		
		try {
            InetAddress myHost = InetAddress.getLocalHost();
            hostname = myHost.getHostName();
        } catch (UnknownHostException ex) {
            logger.error("Unknown Hostname", ex);
        }
		
		return hostname;
	}
	
	@RequestMapping(value = {"/default", "/"}, method = RequestMethod.GET)
	public String defaultPage(Model model, Locale locale) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		String server = getHostName();
		
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("server", server);
		
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
	
	@RequestMapping(value="/gen3des", method = RequestMethod.GET)
	public String gen3DesPage(Model model, RedirectAttributes redirectAttributes){
		
		logger.info("Accessing Gen3Des page...");
		
		redirectAttributes.addFlashAttribute("gen3des","true");
		
		return "redirect:/";
		
	}
	
	@RequestMapping(value="/validate", method =  RequestMethod.POST)
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
	
	@RequestMapping(value="/generate", method =  RequestMethod.POST)
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
	
	@RequestMapping(value="/gen3des", method =  RequestMethod.POST)
	public String gen3DesPage(@Validated Gen3DesBean bean, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
		
		String action = request.getParameter("action");
		
		String output = "";
		
		if(action.equalsIgnoreCase("Encrypt")){
			logger.info("Encryption of String Started...");
			
			bean.setEncDecType("e");
			
			output = Gen3Des.encrypt(bean);
			
			logger.info("Encryption of String End...");
		}else if(action.equalsIgnoreCase("Decrypt")){
			logger.info("Decryption of String Started...");
			
			bean.setEncDecType("d");
			
			output = Gen3Des.decrypt(bean);
			
			logger.info("Decryption of String End...");
		}
		
		redirectAttributes.addFlashAttribute("gen3des","true");
		redirectAttributes.addFlashAttribute("msg", output);
		return "redirect:/";
	}
	
	@RequestMapping(value="/getEmployee", method = RequestMethod.GET)
	public String getEmployeePage(Model model){
		
		List<Map<String, Object>> list = employeeBO.getAllEmployees();
		
		Set<String> headers = new HashSet<String>();
		
		if(list != null && list.size() > 0){
			headers = list.get(0).keySet();
		}
		
		model.addAttribute("headers", headers);
		model.addAttribute("details", list);
		
		return "issuerDetails";
	}
	
	@RequestMapping(value="/getIssuerDetails", method = RequestMethod.GET)
	public String getIssuerDetailsPage(Model model, RedirectAttributes redirectAttributes){
		
		logger.info("Accessing Issuer Details page...");
		
		redirectAttributes.addFlashAttribute("issuerDetails","true");
		
		return "redirect:/";
		
	}
	
	@RequestMapping(value="/getIssuerDetails", method = RequestMethod.POST)
	public String getIssuerDetailsPage(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
		
		String bankdirname = request.getParameter("bankdirname");
		
		if(bankdirname != null && bankdirname.isEmpty()){
			redirectAttributes.addFlashAttribute("msg", "Bankdirname cannot be Empty");
			redirectAttributes.addFlashAttribute("issuerDetails","true");
			
			return "redirect:/";
		}
		
		logger.info("Getting Callout Details...");
		List<Map<String, Object>> list_callouts = issuerBO.getCalloutDetails(bankdirname);
		
		logger.info("Getting RA Details...");
		List<Map<String, Object>> list_ra = issuerBO.getRADetails(bankdirname);
		
		logger.info("Getting Content Details...");
		List<Map<String, Object>> list_contents = issuerBO.getContentDetails(bankdirname);
		
		logger.info("Getting CAP Details...");
		List<Map<String, Object>> list_cap = issuerBO.getCAPDetails(bankdirname);
		
		/*List<Map<String, Object>> list = employeeBO.getAllEmployees();
		
		Set<String> headers = new HashSet<String>();
		
		if(list != null && list.size() > 0){
			headers = list.get(0).keySet();
		}*/
		
		Set<String> headers_callouts = new HashSet<String>();
		Set<String> headers_ra = new HashSet<String>();
		Set<String> headers_contents = new HashSet<String>();
		Set<String> headers_cap = new HashSet<String>();
		
		boolean flag = false;
		
		if(list_callouts != null && list_callouts.size() > 0){
			headers_callouts = list_callouts.get(0).keySet();
			flag = true;
		}
		
		if(list_ra != null && list_ra.size() > 0){
			headers_ra = list_ra.get(0).keySet();
			flag = true;
		}
		
		if(list_contents != null && list_contents.size() > 0){
			headers_contents = list_contents.get(0).keySet();
			flag = true;
		}
		
		if(list_cap != null && list_cap.size() > 0){
			headers_cap = list_cap.get(0).keySet();
			flag = true;
		}
		
		redirectAttributes.addFlashAttribute("headers_callouts", headers_callouts);
		redirectAttributes.addFlashAttribute("headers_ra", headers_ra);
		redirectAttributes.addFlashAttribute("headers_contents", headers_contents);
		redirectAttributes.addFlashAttribute("headers_cap", headers_cap);
		
		redirectAttributes.addFlashAttribute("list_callouts", list_callouts);
		redirectAttributes.addFlashAttribute("list_ra", list_ra);
		redirectAttributes.addFlashAttribute("list_contents", list_contents);
		redirectAttributes.addFlashAttribute("list_cap", list_cap);
		
		/*redirectAttributes.addFlashAttribute("list_e", list);
		redirectAttributes.addFlashAttribute("headers_e", headers);*/
		
		if(flag){
			redirectAttributes.addFlashAttribute("msg", "");
		}else{
			redirectAttributes.addFlashAttribute("msg", "No Record Found!!!");
		}
		
		
		redirectAttributes.addFlashAttribute("issuerDetails","true");
		
		return "redirect:/";
		
	}
	
}
