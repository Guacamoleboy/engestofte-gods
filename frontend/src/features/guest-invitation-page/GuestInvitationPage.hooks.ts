// Pathing
// _______
// src/features/guest-invitation-page/GuestInvitationPage.hooks.ts

import { useCallback, useEffect, useState } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import { getGuestInvitation, type GuestInvitation } from '../../api/endpoints/events'

export function useGuestInvitation() {
	const { id: rawId } = useParams()
	const [searchParams] = useSearchParams()
	const [invitation, setInvitation] = useState<GuestInvitation | null>(null)
	const [state, setState] = useState<'loading' | 'loaded' | 'unavailable'>('loading')
	const access = searchParams.get('access')

	const loadInvitation = useCallback(async () => {
		const eventId = Number(rawId)
		if (!Number.isInteger(eventId) || eventId < 1 || !access) {
			setState('unavailable')
			return
		}
		setState('loading')
		try {
			setInvitation(await getGuestInvitation(eventId, access))
			setState('loaded')
		} catch {
			setState('unavailable')
		}
	}, [access, rawId])

	useEffect(() => { void loadInvitation() }, [loadInvitation])

	return { invitation, loadInvitation, state }
}
