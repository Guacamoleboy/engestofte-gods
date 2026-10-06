// Pathing
// _______
// src/api/endpoints/myEnquiries.ts

import { client } from '../client'

export type EnquirySummary = {
	submission_id: string
	event_id: number | null
	language: 'da' | 'en' | 'de'
	status: 'SUBMITTED' | 'UNDER_REVIEW' | 'AWAITING_CUSTOMER' | 'FOLLOW_UP_REQUIRED' | 'OWNER_FOLLOW_UP_REQUIRED' | 'APPROVED' | 'AWAITING_DEPOSIT' | 'BOOKED' | 'CLOSED_BY_OWNER' | 'CLOSED_BY_CUSTOMER' | 'CANCELLED_BY_CUSTOMER'
	submitted_at: string
	customer_question: string | null
}

type ApiEnvelope<T> = {
	data: T
}

export async function getMyEnquiries() {
	const response = await client<ApiEnvelope<EnquirySummary[]>>('/enquiries')
	return response.data
}

export async function closeMyEnquiry(submissionId: string) {
	return client<ApiEnvelope<string>>(`/enquiries/${encodeURIComponent(submissionId)}/close`, { method: 'POST' })
}
