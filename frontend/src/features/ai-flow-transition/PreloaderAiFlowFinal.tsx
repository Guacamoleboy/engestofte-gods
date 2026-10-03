// Pathing
// _______
// src/features/ai-flow-transition/PreloaderAiFlowFinal.tsx

import styles from './PreloaderAiFlow.module.css'

type PreloaderAiFlowFinalProps = { phase: 'filling' | 'revealing' }

export default function PreloaderAiFlowFinal({ phase }: PreloaderAiFlowFinalProps) {
	return <div className={`${styles.overlay} ${styles.final} ${phase === 'revealing' ? styles.revealing : ''}`} aria-hidden="true">
		<div className={styles.fill} />
	</div>
}
