// Pathing
// _______
// src/api/client.ts

// Local development API base URL; replace with the deployment API URL per environment.
const BASE_URL = 'http://localhost:7070/v1'

type ErrorResponse = {
	message?: string
}

export class ApiError extends Error {
	readonly status: number
	readonly data: unknown

	constructor(message: string, status: number, data: unknown) {
		super(message)
		this.name = 'ApiError'
		this.status = status
		this.data = data
	}
}

export async function client<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
	const url = `${BASE_URL}${endpoint}`
	const token = localStorage.getItem('access_token')
	const headers = new Headers(options.headers)
	if (!headers.has('Content-Type')) headers.set('Content-Type', 'application/json')
	if (token && !headers.has('Authorization')) headers.set('Authorization', `Bearer ${token}`)
	const response = await fetch(url, {
		...options,
		headers,
	})

	let data: unknown = null
	try {
		data = await response.json()
	} catch {
		data = null
	}

	if (!response.ok) {
		const message = isErrorResponse(data) ? data.message : undefined
		throw new ApiError(message ?? 'API request failed', response.status, data)
	}

	return data as T
}

function isErrorResponse(value: unknown): value is ErrorResponse {
	return typeof value === 'object' && value !== null && 'message' in value && typeof value.message === 'string'
}