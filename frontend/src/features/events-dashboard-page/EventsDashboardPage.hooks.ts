// Pathing
// _______
// src/features/events-dashboard-page/EventsDashboardPage.hooks.ts

import { useCallback, useEffect, useState } from 'react'
import { ApiError } from '../../api/client'
import { getMyEnquiries, type EnquirySummary } from '../../api/endpoints/myEnquiries'

export function useEventsDashboard() {
	const [enquiries, setEnquiries] = useState<EnquirySummary[]>([])
	const [state, setState] = useState<'loading' | 'loaded' | 'unauthenticated' | 'error'>('loading')

	const loadEnquiries = useCallback(async () => {
		setState('loading')
		try {
			setEnquiries(await getMyEnquiries())
			setState('loaded')
		} catch (error) {
			setState(error instanceof ApiError && (error.status === 401 || error.status === 403) ? 'unauthenticated' : 'error')
		}
	}, [])

	useEffect(() => {
		void loadEnquiries()
	}, [loadEnquiries])

	return { enquiries, loadEnquiries, state }
}
