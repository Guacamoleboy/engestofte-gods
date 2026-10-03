// Pathing
// _______
// src/shared/data/authSession.ts

export type AccountRole = 'CUSTOMER' | 'STAFF' | 'OWNER'

export function getAccountRole(): AccountRole | null {
	const storedRole = window.localStorage.getItem('account_role')
	if (isAccountRole(storedRole)) return storedRole

	const token = window.localStorage.getItem('access_token')
	if (!token) return null
	try {
		const encodedPayload = token.split('.')[1]
		if (!encodedPayload) return null
		const base64Payload = encodedPayload.replace(/-/g, '+').replace(/_/g, '/')
		const binaryPayload = window.atob(base64Payload.padEnd(Math.ceil(base64Payload.length / 4) * 4, '='))
		const payload = JSON.parse(new TextDecoder().decode(Uint8Array.from(binaryPayload, (character) => character.charCodeAt(0)))) as { role?: unknown }
		return isAccountRole(payload.role) ? payload.role : null
	} catch {
		return null
	}
}

export function clearAuthSession() {
	window.localStorage.removeItem('access_token')
	window.localStorage.removeItem('refresh_token')
	window.localStorage.removeItem('account_role')
	window.localStorage.removeItem('engestofte.pendingSubmissionId')
}

function isAccountRole(value: unknown): value is AccountRole {
	return value === 'CUSTOMER' || value === 'STAFF' || value === 'OWNER'
}
