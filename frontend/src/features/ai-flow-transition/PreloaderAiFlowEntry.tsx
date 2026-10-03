// Pathing
// _______
// src/features/ai-flow-transition/PreloaderAiFlowEntry.tsx

import styles from './PreloaderAiFlow.module.css'

type PreloaderAiFlowEntryProps = { phase: 'filling' | 'revealing' }

export default function PreloaderAiFlowEntry({ phase }: PreloaderAiFlowEntryProps) {
	return <div className={`${styles.overlay} ${styles.entry} ${phase === 'revealing' ? styles.revealing : ''}`} aria-hidden="true">
		<div className={styles.fill} />
		<img className={styles.logo} src="/images/shared/logo-white.png" alt="" />
	</div>
}
