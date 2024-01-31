import React, {useRef} from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import TextInput from "../UI/TextInput/TextInput";
import Button from "../UI/Button/Button";

const ChangeCustomerPasswordForm = ({ customer, isHidden, setIsHidden, onChange}) => {
    const customerPasswordRef = useRef(null);

    async function changeCustomerPassword() {
        await CsrfFetch(`${await CentralServerLinksProvider.getLink("customer-patch")}`.replace('{name}', customer.name), {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify([{
                'op': 'replace',
                'path': '/passwordHash',
                'value': customerPasswordRef.current.value
            }])
        });
        await onChange();
        setIsHidden(true);
    }

    return (
        <PopUpForm header={'Сменить пароль пользователя'} isHidden={isHidden}>
            <TextInput type={'text'} placeholder={'Имя'} inputRef={customerPasswordRef}/>
            <Button text={'Применить'} onClick={changeCustomerPassword}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default ChangeCustomerPasswordForm;