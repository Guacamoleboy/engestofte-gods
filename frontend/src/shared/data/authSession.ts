// Pathing
// _______
// src/shared/data/authSession.ts

export type AccountRole = 'CUSTOMER' | 'STAFF' | 'OWNER'

export function getDashboardPath(role: AccountRole) {
	if (role === 'OWNER') return '/owner/requests'
	if (role === 'STAFF') return '/staff/events'
	return '/dashboard/events/'
}

export function canAccessDashboardPath(pathname: string, role: AccountRole) {
	const allowedSection = role === 'OWNER'
		? '/owner/requests'
		: role === 'STAFF'
			? '/staff/events'
			: '/dashboard/events'
	return pathname === allowedSection || pathname.startsWith(`${allowedSection}/`)
}

export function clearAuthSession() {
	window.localStorage.removeItem('access_token')
	window.localStorage.removeItem('refresh_token')
	window.localStorage.removeItem('account_role')
	window.localStorage.removeItem('account')
	window.localStorage.removeItem('engestofte.pendingSubmissionId')
}
