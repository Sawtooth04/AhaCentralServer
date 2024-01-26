import React from 'react';
import styles from './style.module.css'

const ErrorMessage = ({ message, isHidden }) => {
    return (
        <p className={isHidden ? styles.errorMessageHidden : styles.errorMessage}>
            {message}
        </p>
    );
};

export default ErrorMessage;