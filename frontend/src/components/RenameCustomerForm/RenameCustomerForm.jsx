import React, {useRef} from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import TextInput from "../UI/TextInput/TextInput";
import Button from "../UI/Button/Button";

const RenameCustomerForm = ({ customer, isHidden, setIsHidden, onRename}) => {
    const customerNameRef = useRef(null);

    async function renameCustomer() {
        await CsrfFetch(`${await CentralServerLinksProvider.getLink("customer-patch")}`.replace('{name}', customer.name), {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify([{
                'op': 'replace',
                'path': '/name',
                'value': customerNameRef.current.value
            }])
        });
        await onRename();
        setIsHidden(true);
    }

    return (
        <PopUpForm header={'Переименовать пользователя'} isHidden={isHidden}>
            <TextInput type={'text'} placeholder={'Имя'} inputRef={customerNameRef}/>
            <Button text={'Применить'} onClick={renameCustomer}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default RenameCustomerForm;