// Pathing
// _______
// src/api/endpoints/auth.ts

import { client } from '../client'

export type AuthResponse = {
	access_token: string
	refresh_token: string
	account: {
		full_name: string
		email_redacted: string
		role: 'CUSTOMER' | 'STAFF' | 'OWNER'
	}
}

type ApiEnvelope<T> = {
	data: T
}

export async function login(email: string, password: string) {
	const response = await client<ApiEnvelope<AuthResponse>>('/auth/login', {
		method: 'POST',
		body: JSON.stringify({ email, password }),
	})
	return response.data
}

export async function register(fullName: string, email: string, password: string) {
	const response = await client<ApiEnvelope<AuthResponse>>('/auth/register', {
		method: 'POST',
		body: JSON.stringify({ full_name: fullName, email, password }),
	})
	return response.data
}
