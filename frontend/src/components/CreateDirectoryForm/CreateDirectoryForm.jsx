import React, {useRef} from 'react';
import TextInput from "../UI/TextInput/TextInput";
import Button from "../UI/Button/Button";
import PopUpForm from "../UI/PopUpForm/PopUpForm";

const CreateDirectoryForm = ({ setPathParts, pathParts, isHidden, setIsHidden }) => {
    const directoryNameRef = useRef(null);

    function addDirectory() {
        let name = directoryNameRef.current.value;

        if (name !== '') {
            setPathParts([...pathParts, name]);
            setIsHidden(true);
        }
    }

    return (
        <PopUpForm header={'Создание папки'} isHidden={isHidden}>
            <TextInput type={'text'} placeholder={'Название'} inputRef={directoryNameRef}/>
            <Button text={'Создать'} onClick={addDirectory}/>
            <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
        </PopUpForm>
    );
};

export default CreateDirectoryForm;