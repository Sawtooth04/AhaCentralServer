import React, {useRef} from 'react';
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import TextInput from "../UI/TextInput/TextInput";
import Button from "../UI/Button/Button";

const RenameGroupForm = ({ group, isHidden, setIsHidden, onRename}) => {
    const groupNameRef = useRef(null);

    async function renameGroup() {
        await CsrfFetch(`${await CentralServerLinksProvider.getLink("group-patch")}/${group.groupID}`, {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify([{
                'op': 'replace',
                'path': '/name',
                'value': groupNameRef.current.value
            }])
        });
        await onRename();
        setIsHidden(true);
    }

    return (
        <PopUpForm header={'Переименовать группу'} isHidden={isHidden}>
            <TextInput type={'text'} placeholder={'Название'} inputRef={groupNameRef}/>
            <Button text={'Применить'} onClick={renameGroup}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default RenameGroupForm;