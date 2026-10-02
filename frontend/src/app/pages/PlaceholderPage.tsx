// Pathing
// _______
// src/app/pages/PlaceholderPage.tsx

import PlaceholderPages from '../../features/placeholder-pages'

type PlaceholderPageProps = {
	route: string
}

export default function PlaceholderPage({ route }: PlaceholderPageProps) {
	return <PlaceholderPages route={route} />
}
