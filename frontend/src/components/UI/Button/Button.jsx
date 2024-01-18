import React from 'react';
import styles from './style.module.css'

const Button = ({ onClick, text }) => {
    return (
        <button className={styles.button} onClick={onClick}>
            {text}
        </button>
    );
};

export default Button;