package htwp

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService


@Secured(['ROLE_ADMIN', 'ROLE_SERVER'])
class ApiController {
	
    SpringSecurityService springSecurityService

    def index() { 
	render springSecurityService.currentUser as JSON
    }
}

