// Pathing
// _______
// src/api/endpoints/myEnquiries.ts

import { client } from '../client'

export type EnquirySummary = {
	submission_id: string
	language: 'da' | 'en' | 'de'
	status: 'SUBMITTED' | 'UNDER_REVIEW' | 'AWAITING_CUSTOMER' | 'APPROVED' | 'CANCELLED_BY_CUSTOMER'
	submitted_at: string
}

type ApiEnvelope<T> = {
	data: T
}

export async function getMyEnquiries() {
	const response = await client<ApiEnvelope<EnquirySummary[]>>('/enquiries')
	return response.data
}
