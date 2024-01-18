import React from 'react';
import styles from './style.module.css'

const TextInput = ({ placeholder, inputRef, type }) => {
    return (
        <input className={styles.textInput} type={type} placeholder={placeholder} ref={inputRef}/>
    );
};

export default TextInput;