// Pathing
// _______
// src/features/events-dashboard-page/EventsDashboardPage.hooks.ts

import { useCallback, useEffect, useState } from 'react'
import { ApiError } from '../../api/client'
import { getMyEnquiries, type EnquirySummary } from '../../api/endpoints/myEnquiries'
import { getImportantMessages, type ImportantMessage } from '../../api/endpoints/events'

export function useEventsDashboard() {
	const [enquiries, setEnquiries] = useState<EnquirySummary[]>([])
	const [importantMessages, setImportantMessages] = useState<ImportantMessage[]>([])
	const [state, setState] = useState<'loading' | 'loaded' | 'unauthenticated' | 'error'>('loading')

	const loadEnquiries = useCallback(async () => {
		setState('loading')
		try {
			const [loadedEnquiries, loadedImportantMessages] = await Promise.all([getMyEnquiries(), getImportantMessages()])
			setEnquiries(loadedEnquiries)
			setImportantMessages(loadedImportantMessages)
			setState('loaded')
		} catch (error) {
			setState(error instanceof ApiError && (error.status === 401 || error.status === 403) ? 'unauthenticated' : 'error')
		}
	}, [])

	useEffect(() => {
		void loadEnquiries()
	}, [loadEnquiries])

	return { enquiries, importantMessages, loadEnquiries, state }
}
