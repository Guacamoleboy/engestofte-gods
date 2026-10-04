// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestsPage.hooks.ts

import { useCallback, useEffect, useState } from 'react'
import { getOwnerEnquiries, type OwnerEnquirySummary } from '../../api/endpoints/ownerEnquiries'

export function useOwnerEnquiries() {
	const [enquiries, setEnquiries] = useState<OwnerEnquirySummary[]>([])
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')

	const loadEnquiries = useCallback(async () => {
		setState('loading')
		try {
			setEnquiries(await getOwnerEnquiries())
			setState('loaded')
		} catch {
			setState('error')
		}
	}, [])

	useEffect(() => {
		void loadEnquiries()
	}, [loadEnquiries])

	return { enquiries, loadEnquiries, state }
}
