import React, {useRef, useState} from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import styles from "./style.module.css";
import TextInput from "../UI/TextInput/TextInput";
import Button from "../UI/Button/Button";
import ErrorMessage from "../UI/ErrorMessage/ErrorMessage";

const Registration = () => {
    const [errors, setErrors] = useState({
        'isNameFree': true,
        'isNameValid': true,
        'isPasswordValid': true
    });
    const loginRef = useRef(null);
    const passwordRef = useRef(null);

    async function registration() {
        let response = await CsrfFetch(await CentralServerLinksProvider.getLink('registration'), {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                name: loginRef.current.value,
                password: passwordRef.current.value
            })
        });

        if (response.ok)
            console.log('harosh, harosh')
        else
            setErrors(await response.json());
    }

    return (
        <div className={styles.registration}>
            <img className={styles.registrationLogo} src={'assets/images/logo.png'} alt={'Logo'}/>
            <div className={styles.registrationSignWrapper}>
                <p className={styles.registrationSign}> Aha </p>
                <p className={styles.registrationSign}> Storage </p>
                <p className={styles.registrationSign}> System </p>
            </div>
            <div className={styles.registrationTextInputsWrapper}>
                <TextInput placeholder={"Логин"} type={'text'} inputRef={loginRef}/>
                <TextInput placeholder={"Пароль"} type={'password'} inputRef={passwordRef}/>
                <Button text={'Регистрация'} onClick={registration}/>
                <ErrorMessage isHidden={errors.isNameFree} message={'Имя пользователя уже занято'}/>
                <ErrorMessage isHidden={errors.isNameValid} message={'Имя пользователя должно содержать от 8 до 32 символов'}/>
                <ErrorMessage isHidden={errors.isPasswordValid} message={'Пароль пользователя должен содержать от 8 до 20 символов'}/>
            </div>
        </div>
    );
};

export default Registration;