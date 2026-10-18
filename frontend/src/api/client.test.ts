// Pathing
// _______
// src/api/client.test.ts

import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { ApiError, client } from './client'

describe('API client', () => {

	beforeEach(() => {
		localStorage.clear()
	})

	// ------------------------------------------------------------------------------------------------------

	afterEach(() => {
		vi.unstubAllGlobals()
	})

	// ------------------------------------------------------------------------------------------------------

	it('sends JSON requests with the access token and returns the response body', async () => {
		localStorage.setItem('access_token', 'test-token')
		const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({ data: 'ok' }), {
			status: 200,
			headers: { 'Content-Type': 'application/json' },
		}))
		vi.stubGlobal('fetch', fetchMock)

		const result = await client<{ data: string }>('/enquiries', {
			method: 'POST',
			body: JSON.stringify({ name: 'Alex' }),
		})

		expect(result).toEqual({ data: 'ok' })
		expect(fetchMock).toHaveBeenCalledOnce()
		const [url, request] = fetchMock.mock.calls[0] as [string, RequestInit]
		expect(url).toBe('http://localhost:7070/v1/enquiries')
		expect(request.method).toBe('POST')
		expect(new Headers(request.headers).get('Content-Type')).toBe('application/json')
		expect(new Headers(request.headers).get('Authorization')).toBe('Bearer test-token')
	})

	// ------------------------------------------------------------------------------------------------------

	it('preserves caller-provided authorization and content type headers', async () => {
		const fetchMock = vi.fn().mockResolvedValue(new Response('{}', { status: 200 }))
		vi.stubGlobal('fetch', fetchMock)

		await client('/resource', {
			headers: { Authorization: 'Bearer custom', 'Content-Type': 'text/plain' },
		})

		const request = fetchMock.mock.calls[0]?.[1] as RequestInit
		expect(new Headers(request.headers).get('Authorization')).toBe('Bearer custom')
		expect(new Headers(request.headers).get('Content-Type')).toBe('text/plain')
	})

	// ------------------------------------------------------------------------------------------------------

	it('throws ApiError with the server message and error code', async () => {
		vi.stubGlobal('fetch', vi.fn().mockResolvedValue(new Response(JSON.stringify({
			message: 'Invalid request',
			code: 422,
		}), { status: 400 })))

		await expect(client('/enquiries')).rejects.toMatchObject({
			name: 'ApiError',
			message: 'Invalid request',
			status: 400,
			code: 422,
			data: { message: 'Invalid request', code: 422 },
		})
	})

	// ------------------------------------------------------------------------------------------------------

	it('uses the HTTP status and fallback message for non-JSON errors', async () => {
		vi.stubGlobal('fetch', vi.fn().mockResolvedValue(new Response('Service unavailable', { status: 503 })))
		const request = client('/enquiries')

		await expect(request).rejects.toBeInstanceOf(ApiError)
		await expect(request).rejects.toMatchObject({
			message: 'API request failed',
			status: 503,
			code: 503,
			data: null,
		})
	})

	// ------------------------------------------------------------------------------------------------------

	it('returns null for successful responses without JSON', async () => {
		vi.stubGlobal('fetch', vi.fn().mockResolvedValue(new Response(null, { status: 204 })))
		await expect(client('/resource')).resolves.toBeNull()
	})

})