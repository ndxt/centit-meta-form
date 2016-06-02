package com.centit.metaform.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.metaform.fromaccess.ModelFormService;

@Controller
@RequestMapping("/metaform/formaccess")
public class MetaFormController  extends BaseController{
	private static final Log log = LogFactory.getLog(MetaFormController.class);

	@Resource
	private ModelFormService formService;
	
	@RequestMapping(value = "/list/{modelCode}",method = RequestMethod.GET)
	public void list(@PathVariable String modelCode, PageDesc pageDesc ,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
		
	}
	
	@RequestMapping(value = "/view/{modelCode}",method = RequestMethod.GET)
	public void view(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {
       
		
	}
	
	@RequestMapping(value = "/meta/{modelCode}",method = RequestMethod.GET)
	public void meta(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {
       
		
	}
	
	@RequestMapping(value = "/create/{modelCode}",method = RequestMethod.POST)
	public void create(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {
       
		
	}
	
	@RequestMapping(value = "/update/{modelCode}",method = RequestMethod.PUT)
	public void update(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {
        
		
	}
	
	@RequestMapping(value = "/delete/{modelCode}",method = RequestMethod.DELETE)
	public void delete(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {
        
		
	}
	
	@RequestMapping(value = "/submit/{modelCode}",method = RequestMethod.POST)
	public void submit(@PathVariable String modelCode, HttpServletRequest request, HttpServletResponse response) {
       
		
	}
	
}
