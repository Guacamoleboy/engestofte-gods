// Pathing
// _______
// src/api/endpoints/enquiries.ts

import { client } from '../client'

export type EnquirySubmission = {
	submission_id: string
	language: string
	draft: Record<string, unknown>
}

type ApiEnvelope<T> = {
	data: T
}

export async function submitEnquiry(submission: EnquirySubmission) {
	return client<ApiEnvelope<{ submission_id: string; language: string; status: string }>>('/enquiries', {
		method: 'POST',
		body: JSON.stringify(submission),
	})
}
