import React from 'react';
import styles from './style.module.css';

const HintButton = ({text, src, hint, onClick, isActive }) => {
    function onClickCallback() {
        if (isActive)
            onClick();
    }

    return (
        <div className={isActive ? styles.hintButton : styles.hintButtonDisable} onClick={onClickCallback}>
            {typeof text !== 'undefined' ? <p className={styles.hintButtonText}> {text} </p> :
                <img className={styles.hintButtonImage} src={src} alt={"Icon"}/>
            }
            <p className={styles.hintButtonHint}> {hint} </p>
        </div>
    );
};

export default HintButton;