package htCommon

import grails.core.GrailsApplication;
import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class HtUserHtRole implements Serializable {

	private static final long serialVersionUID = 1

	public static initDefaultUsers(GrailsApplication grailsApplication) {
		def patientRole = new HtRole("ROLE_PATIENT").save();
		def adminRole = new HtRole("ROLE_ADMIN").save()
		def doctorRole = new HtRole("ROLE_DOCTOR").save()
		def nurseRole = new HtRole("ROLE_NURSE").save()
		def serverRole = new HtRole("ROLE_SERVER").save()
		
		def user1 = new HtUser("admin", "password").save()
		HtUserHtRole.create user1, adminRole, true
		
		def user2 = new HtUser("doctor", "password").save()
		HtUserHtRole.create user2, doctorRole, true
		
		def user3 = new HtUser("nurse", "password").save()
		HtUserHtRole.create user3, nurseRole, true
	}
	
	HtUser htUser
	HtRole htRole

	HtUserHtRole(HtUser u, HtRole r) {
		this()
		htUser = u
		htRole = r
	}

	@Override
	boolean equals(other) {
		if (!(other instanceof HtUserHtRole)) {
			return false
		}

		other.htUser?.id == htUser?.id && other.htRole?.id == htRole?.id
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (htUser) builder.append(htUser.id)
		if (htRole) builder.append(htRole.id)
		builder.toHashCode()
	}

	static HtUserHtRole get(long htUserId, long htRoleId) {
		criteriaFor(htUserId, htRoleId).get()
	}

	static boolean exists(long htUserId, long htRoleId) {
		criteriaFor(htUserId, htRoleId).count()
	}

	private static DetachedCriteria criteriaFor(long htUserId, long htRoleId) {
		HtUserHtRole.where {
			htUser == HtUser.load(htUserId) &&
			htRole == HtRole.load(htRoleId)
		}
	}

	static HtUserHtRole create(HtUser htUser, HtRole htRole, boolean flush = false) {
		def instance = new HtUserHtRole(htUser: htUser, htRole: htRole)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(HtUser u, HtRole r, boolean flush = false) {
		if (u == null || r == null) return false

		int rowCount = HtUserHtRole.where { htUser == u && htRole == r }.deleteAll()

		if (flush) { HtUserHtRole.withSession { it.flush() } }

		rowCount
	}

	static void removeAll(HtUser u, boolean flush = false) {
		if (u == null) return

		HtUserHtRole.where { htUser == u }.deleteAll()

		if (flush) { HtUserHtRole.withSession { it.flush() } }
	}

	static void removeAll(HtRole r, boolean flush = false) {
		if (r == null) return

		HtUserHtRole.where { htRole == r }.deleteAll()

		if (flush) { HtUserHtRole.withSession { it.flush() } }
	}

	static constraints = {
		htRole validator: { HtRole r, HtUserHtRole ur ->
			if (ur.htUser == null || ur.htUser.id == null) return
			boolean existing = false
			HtUserHtRole.withNewSession {
				existing = HtUserHtRole.exists(ur.htUser.id, r.id)
			}
			if (existing) {
				return 'userRole.exists'
			}
		}
	}

	static mapping = {
		id composite: ['htUser', 'htRole']
		version false
	}
}
