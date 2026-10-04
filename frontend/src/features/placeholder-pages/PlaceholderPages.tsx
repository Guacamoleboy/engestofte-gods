// Pathing
// _______
// src/features/placeholder-pages/PlaceholderPages.tsx

import { Link } from 'react-router-dom'
import { Button, Card, EmptyState } from '../../shared/components/Ui'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import styles from './PlaceholderPages.module.css'

type PlaceholderPagesProps = {
	route: string
}

export default function PlaceholderPages({ route }: PlaceholderPagesProps) {
	const { content: copy } = useTranslate()
	const title = route === '404' ? copy.placeholder.notFound : copy.placeholder.routes[route] ?? 'Engestofte Gods'

	return (
		<PageContainer className={styles.placeholderPage}>
			<p className={styles.eyebrow}>{copy.placeholder.eyebrow}</p>
			<h1>{title}</h1>
			<Card className={styles.placeholderCard}>
				<EmptyState
					title={route === '404' ? copy.placeholder.notFound : copy.placeholder.comingSoon}
					description={route === '404' ? copy.placeholder.notFoundDescription : copy.placeholder.comingSoonDescription.replace('{title}', title)}
					action={<Button as="link" to="/kontakt" variant="secondary">{copy.placeholder.backToContact}</Button>}
				/>
			</Card>
			{route !== '404' && <p className={styles.routeCaption}>{copy.placeholder.routeLabel}: <code>{route}</code></p>}
			<Link className="visually-hidden" to="/kontakt">Engestofte Gods</Link>
		</PageContainer>
	)
}
