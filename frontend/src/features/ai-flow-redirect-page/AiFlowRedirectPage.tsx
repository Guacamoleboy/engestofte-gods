import { Link, useLocation } from 'react-router-dom'
import { useTranslate } from '../../shared/hooks/useTranslate'
import styles from './AiFlowRedirectPage.module.css'

export default function AiFlowRedirectPage() {
	const { content } = useTranslate()
	const location = useLocation()
	const copy = content.aiFlow.redirect
	const customerName = typeof location.state?.customerName === 'string' ? location.state.customerName : ''
	const title = copy.title.replace('{name}, ', customerName ? `${customerName}, ` : '')

	return (
		<main className={styles.page}>
			<div className={styles.content}>
				<img className={styles.logo} src="/images/shared/logo-white.png" alt="Engestofte Gods" />
				<h1>{title}</h1>
				<p>{copy.description}</p>
				<p>{copy.contactExplanation}</p>
				<div className={styles.actions}>
					<Link className={styles.createAccount} to="/register">{copy.createAccount}</Link>
					<button className={styles.sendEmail} type="button" disabled>
						{copy.sendEmail}
					</button>
					<Link className={styles.cancelEnquiry} to="/kontakt">{copy.cancelEnquiry}</Link>
				</div>
			</div>
		</main>
	)
}
