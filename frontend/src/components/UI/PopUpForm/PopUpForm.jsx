import React from 'react';
import styles from './style.module.css'

const PopUpForm = ({header, isHidden, children }) => {
    return (
        <div className={isHidden ? styles.hiddenPopUpForm : styles.popUpForm}>
            <div className={styles.popUpFormContent}>
                <h1 className={styles.popUpFormContentHeader}> {header} </h1>
                {children}
            </div>
        </div>
    );
};

export default PopUpForm;