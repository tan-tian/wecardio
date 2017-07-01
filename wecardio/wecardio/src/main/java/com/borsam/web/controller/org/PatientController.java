package com.borsam.web.controller.org;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 患者管理
 * Created by Sebarswee on 2015/7/7.
 */
@Controller("orgPatientController")
@RequestMapping("/org/patient")
public class PatientController extends com.borsam.web.controller.admin.PatientController {}
