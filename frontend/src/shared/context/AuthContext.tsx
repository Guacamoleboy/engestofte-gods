// Pathing
// _______
// src/shared/context/AuthContext.tsx

import { createContext, useCallback, useState, type PropsWithChildren } from 'react'
import type { AuthResponse } from '../../api/endpoints/auth'
import { clearAuthSession, type AccountRole } from '../data/authSession'

export type AuthUser = {
	fullName: string
	email: string
	role: AccountRole
}

type AuthContextValue = {
	user: AuthUser | null
	isAuthenticated: boolean
	setSession: (auth: AuthResponse) => void
	clearSession: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: PropsWithChildren) {
	const [user, setUser] = useState<AuthUser | null>(getStoredUser)

	const setSession = useCallback((auth: AuthResponse) => {
		window.localStorage.setItem('access_token', auth.access_token)
		window.localStorage.setItem('refresh_token', auth.refresh_token)
		window.localStorage.setItem('account_role', auth.account.role)
		window.localStorage.setItem('account', JSON.stringify(auth.account))
		setUser(getStoredUser())
	}, [])

	const clearSession = useCallback(() => {
		clearAuthSession()
		setUser(null)
	}, [])

	return (
		<AuthContext.Provider value={{ user, isAuthenticated: user !== null, setSession, clearSession }}>
			{children}
		</AuthContext.Provider>
	)
}

function getStoredUser(): AuthUser | null {
	const token = window.localStorage.getItem('access_token')
	if (!token) return null
	try {
		const payload = decodeJwtPayload(token)
		if (payload.type !== 'access' || typeof payload.exp !== 'number' || payload.exp * 1000 <= Date.now()) {
			clearAuthSession()
			return null
		}
		const role = isAccountRole(payload.role) ? payload.role : getStoredRole()
		if (!role) {
			clearAuthSession()
			return null
		}
		const account = JSON.parse(window.localStorage.getItem('account') ?? 'null') as unknown
		return {
			fullName: getAccountField(account, 'full_name'),
			email: getAccountField(account, 'email'),
			role,
		}
	} catch {
		clearAuthSession()
		return null
	}
}

function decodeJwtPayload(token: string): Record<string, unknown> {
	const encodedPayload = token.split('.')[1]
	if (!encodedPayload) throw new Error('Invalid access token')
	const base64Payload = encodedPayload.replace(/-/g, '+').replace(/_/g, '/')
	const binaryPayload = window.atob(base64Payload.padEnd(Math.ceil(base64Payload.length / 4) * 4, '='))
	return JSON.parse(new TextDecoder().decode(Uint8Array.from(binaryPayload, (character) => character.charCodeAt(0)))) as Record<string, unknown>
}

function getStoredRole(): AccountRole | null {
	const role = window.localStorage.getItem('account_role')
	return isAccountRole(role) ? role : null
}

function isAccountRole(value: unknown): value is AccountRole {
	return value === 'CUSTOMER' || value === 'STAFF' || value === 'OWNER'
}

function getAccountField(account: unknown, field: 'full_name' | 'email'): string {
	if (typeof account !== 'object' || account === null || !(field in account)) return ''
	const value = (account as Record<string, unknown>)[field]
	return typeof value === 'string' ? value : ''
}
