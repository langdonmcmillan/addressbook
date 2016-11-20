/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.addressbook.controllers;

import com.sg.addressbook.daos.AddressBookDAO;
import com.sg.addressbook.models.Address;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author apprentice
 */
@Controller
public class AddressBookController {
    
    private AddressBookDAO dao;
    
    @Autowired
    @Inject
    public AddressBookController(AddressBookDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(value = {"/", "/home", "addressBook"}, method = RequestMethod.GET)
    public String displayHome(Model model) {
        model.addAttribute("addressList", dao.getAllAddresses());
        return "addressBook";
    }
    
    @RequestMapping(value = {"displayEditAddress"}, method = RequestMethod.POST)
    public String displayEdit(Model model, HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        model.addAttribute("address", dao.getAddress(id));
        model.addAttribute("states", dao.getAllStates());
        return "editAddress";
    }
    
    @RequestMapping(value = {"editAddress"}, method = RequestMethod.POST)
    public String editAddress(@Valid @ModelAttribute("address") Address address, BindingResult result) {
        if (result.hasErrors()) {
            return "editAddress";
        }
        Integer stateID = dao.getStateID(address.getState().getStateName());
        address.getState().setStateID(stateID);
        address.getCity().setStateID(stateID);
        dao.updateAddress(address);
        return "redirect:addressBook";
    }
    
    @RequestMapping(value = {"displayAddAddress"}, method = RequestMethod.GET)
    public String displayAddAddress(Model model) {
        Address address = new Address();
        model.addAttribute("address", address);
        model.addAttribute("states", dao.getAllStates());
        return "addAddress";
    }
    
    @RequestMapping(value = {"addAddress"}, method = RequestMethod.POST)
    public String addAddress(@Valid @ModelAttribute("address") Address address, BindingResult result) {
        if (result.hasErrors()) {
            return "addAddress";
        }
        Integer stateID = dao.getStateID(address.getState().getStateName());
        address.getState().setStateID(stateID);
        address.getCity().setStateID(stateID);
        dao.createAddress(address);
        return "redirect:addressBook";
    }
    
    @RequestMapping(value = {"deleteAddress"}, method = RequestMethod.POST)
    public String deleteAddress(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        dao.deleteAddress(id);
        return "redirect:addressBook";
    }
    
    @RequestMapping(value = {"searchForAddress"}, method = RequestMethod.GET)
    public String searchByName(HttpServletRequest req, Model model) {
        String searchTerm = (req.getParameter("searchTerm")) == null ? "" : req.getParameter("searchTerm");
        String searchType = (req.getParameter("searchType")) == null ? "" : req.getParameter("searchType");
        ArrayList<Address> addressList = new ArrayList<>();
        switch(searchType) {
            case "name":
                addressList = dao.searchByName(searchTerm);
                break;
            case "city":
                addressList = dao.searchByCity(searchTerm);
                break;
            case "state":
                addressList.addAll(dao.searchByState(searchTerm));
                break;
            case "zip":
                addressList = dao.searchByZip(searchTerm);
                break;
            case "" :
                addressList = dao.getAllAddresses();
                break;
        }
        model.addAttribute("addressList", addressList);
        return "addressBook";
    }
}
