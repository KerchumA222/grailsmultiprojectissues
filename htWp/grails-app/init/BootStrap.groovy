import htCommon.HtUserHtRole;

class BootStrap {

	def grailsApplication
	
    def init = { servletContext ->
		
		HtUserHtRole.initDefaultUsers(grailsApplication)
		
    }
    def destroy = {
    }
}
