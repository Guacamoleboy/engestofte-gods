// Pathing
// _______
// src/features/staff-events-page/StaffEventsPage.hooks.ts

import { useCallback, useEffect, useState } from 'react'
import { getStaffEvents, type StaffEvent } from '../../api/endpoints/events'

export function useStaffEvents() {
	const [events, setEvents] = useState<StaffEvent[]>([])
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')

	const loadEvents = useCallback(async () => {
		setState('loading')
		try {
			setEvents(await getStaffEvents())
			setState('loaded')
		} catch {
			setState('error')
		}
	}, [])

	useEffect(() => { void loadEvents() }, [loadEvents])

	return { events, loadEvents, state }
}
