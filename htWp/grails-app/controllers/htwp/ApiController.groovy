package htwp

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured;

@Secured(['ROLE_ADMIN', 'ROLE_SERVER'])
class ApiController {

    def index() { 
		render getAuthenticatedUser() as JSON
	}
}

