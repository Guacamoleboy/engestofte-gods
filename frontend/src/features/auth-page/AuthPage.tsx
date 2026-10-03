// Pathing
// _______
// src/features/auth-page/AuthPage.tsx

import { Link } from 'react-router-dom'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useAuthPage } from './AuthPage.hooks'
import styles from './AuthPage.module.css'

type AuthPageProps = {
	mode: 'login' | 'register' | 'forgot-password'
}

export default function AuthPage({ mode }: AuthPageProps) {
	const { content } = useTranslate()
	const text = content.auth

	if (mode === 'forgot-password') {
		return (
			<main className={styles.page}>
				<section className={styles.card}>
					<p className={styles.eyebrow}>{text.eyebrow}</p>
					<h1>{text.forgotTitle}</h1>
					<p>{text.forgotDescription}</p>
					<Link className={styles.secondaryLink} to="/login">{text.backToLogin}</Link>
				</section>
			</main>
		)
	}

	return <AuthForm mode={mode} />
}

function AuthForm({ mode }: Pick<AuthPageProps, 'mode'>) {
	const { content } = useTranslate()
	const text = content.auth
	const form = useAuthPage(mode)
	const isRegister = mode === 'register'

	return (
		<main className={styles.page}>
			<section className={styles.card}>
				<p className={styles.eyebrow}>{text.eyebrow}</p>
				<h1>{isRegister ? text.registerTitle : text.loginTitle}</h1>
				<p>{isRegister ? text.registerDescription : text.loginDescription}</p>
				<form className={styles.form} onSubmit={form.submit}>
					{isRegister && <label>{text.name}<input autoComplete="name" name="fullName" value={form.fullName} onChange={(event) => form.setFullName(event.target.value)} required /></label>}
					<label>{text.email}<input autoComplete="email" name="email" type="email" value={form.email} onChange={(event) => form.setEmail(event.target.value)} required /></label>
					<label>{text.password}<input autoComplete={isRegister ? 'new-password' : 'current-password'} name="password" type="password" value={form.password} onChange={(event) => form.setPassword(event.target.value)} required /></label>
					{form.error && <p className={styles.error} role="alert">{form.error}</p>}
					<button type="submit" disabled={form.isSubmitting}>{form.isSubmitting ? text.submitting : isRegister ? text.registerSubmit : text.loginSubmit}</button>
				</form>
				<div className={styles.links}>
					{!isRegister && <Link to="/forgot-password">{text.forgotLink}</Link>}
					<Link to={isRegister ? '/login' : '/register'}>{isRegister ? text.haveAccount : text.needAccount}</Link>
				</div>
			</section>
		</main>
	)
}
