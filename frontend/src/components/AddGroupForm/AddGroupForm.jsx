import React, {useRef} from 'react';
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import TextInput from "../UI/TextInput/TextInput";
import Button from "../UI/Button/Button";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";

const AddGroupForm = ({ isHidden, setIsHidden }) => {
    const groupNameRef = useRef(null);

    async function addGroup() {
        await CsrfFetch(await CentralServerLinksProvider.getLink('group-post'), {
            method: 'post',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                'name': groupNameRef.current.value
            })
        });
        setIsHidden(true);
    }

    return (
        <PopUpForm header={'Создание группы'} isHidden={isHidden}>
            <TextInput type={'text'} placeholder={'Название'} inputRef={groupNameRef}/>
            <Button text={'Создать'} onClick={addGroup}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default AddGroupForm;