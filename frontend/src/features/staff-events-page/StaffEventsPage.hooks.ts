// Pathing
// _______
// src/features/staff-events-page/StaffEventsPage.hooks.ts

import { useCallback, useEffect, useState } from 'react'
import { getStaffEvents, getStaffImportantMessages, type ImportantMessage, type StaffEvent } from '../../api/endpoints/events'

export function useStaffEvents() {
	const [events, setEvents] = useState<StaffEvent[]>([])
	const [importantMessages, setImportantMessages] = useState<ImportantMessage[]>([])
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')

	const loadEvents = useCallback(async () => {
		setState('loading')
		try {
			const [loadedEvents, loadedImportantMessages] = await Promise.all([getStaffEvents(), getStaffImportantMessages()])
			setEvents(loadedEvents)
			setImportantMessages(loadedImportantMessages)
			setState('loaded')
		} catch {
			setState('error')
		}
	}, [])

	useEffect(() => { void loadEvents() }, [loadEvents])

	return { events, importantMessages, loadEvents, state }
}
