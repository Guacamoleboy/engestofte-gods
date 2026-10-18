import { afterEach, describe, expect, it, vi } from 'vitest'
import { submitAiAnswer } from './aiFlow'

describe('AI flow endpoint', () => {

	afterEach(() => {
		vi.unstubAllGlobals()
		localStorage.clear()
	})

	// ------------------------------------------------------------------------------------------------------

	it('sends the stable request id with the interaction', async () => {
		const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({ data: {
			acknowledgement: 'Thank you.',
			next_question: 'What date are you considering?',
			customer_name: 'User One',
			step: 1,
			expected_guest_count: null,
			status: 'IN_PROGRESS',
		} }), { status: 200, headers: { 'Content-Type': 'application/json' } }))
		vi.stubGlobal('fetch', fetchMock)

		await submitAiAnswer('b4afc574-8dbb-4588-a76e-bd7f34b9f582', 'User One', 'What is your name?', '', 1, 'en', [])

		const requestBody = JSON.parse(fetchMock.mock.calls[0][1].body as string)
		expect(requestBody.request_id).toBe('b4afc574-8dbb-4588-a76e-bd7f34b9f582')
	})

})