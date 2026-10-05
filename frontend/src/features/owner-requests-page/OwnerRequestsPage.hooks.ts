// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestsPage.hooks.ts

import { useCallback, useEffect, useState } from 'react'
import { getOwnerEnquiries, type OwnerEnquirySummary } from '../../api/endpoints/ownerEnquiries'
import { getOwnerImportantMessages, type ImportantMessage } from '../../api/endpoints/events'

export function useOwnerEnquiries() {
	const [enquiries, setEnquiries] = useState<OwnerEnquirySummary[]>([])
	const [importantMessages, setImportantMessages] = useState<ImportantMessage[]>([])
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')

	const loadEnquiries = useCallback(async () => {
		setState('loading')
		try {
			const [loadedEnquiries, loadedImportantMessages] = await Promise.all([getOwnerEnquiries(), getOwnerImportantMessages()])
			setEnquiries(loadedEnquiries)
			setImportantMessages(loadedImportantMessages)
			setState('loaded')
		} catch {
			setState('error')
		}
	}, [])

	useEffect(() => {
		void loadEnquiries()
	}, [loadEnquiries])

	return { enquiries, importantMessages, loadEnquiries, state }
}
