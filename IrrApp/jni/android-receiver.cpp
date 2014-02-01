#include <android-receiver.h>
#include <android/log.h>


extern int gAppAlive;

bool AndroidEventReceiver::OnEvent(const SEvent& event)
{
    //__android_log_print(ANDROID_LOG_INFO, "Irrlicht", "Lolo");

    if (event.EventType == irr::EET_MOUSE_INPUT_EVENT) {                //�д����¼�����
    	KeyIsPressed = true;
    	KeyIsDown = true;
    	x = event.MouseInput.X;     //��¼����x������
    	y = event.MouseInput.Y;     //��¼����y������
		if(event.MouseInput.Event == irr::EMIE_LMOUSE_PRESSED_DOWN) {   //�����¼�Ϊ�հ���
			ox = x;                 //��¼����ʱ�Ĵ���x������
			oy = y;                 //��¼����ʱ�Ĵ���y������
		}
		else if(event.MouseInput.Event == irr::EMIE_LMOUSE_LEFT_UP) {   //�����¼�Ϊ��ָ�뿪��Ļ
			if( (ox-x) > 30 ) direction = 1;        //�뿪λ��x����������С�ڰ���ʱ��x������
			else if( (x-ox) > 30 ) direction = 2;   //�뿪λ��x���������Դ��ڰ���ʱ��x������
			else direction = 0;     //�����಻����Ϊ�����ƻ���
		}
    }


    if (event.EventType == EET_GUI_EVENT)   //�е����ť�¼�����
    {
        s32 id = event.GUIEvent.Caller->getID();
        IGUIEnvironment* env = Context.device->getGUIEnvironment();

        //__android_log_print(ANDROID_LOG_INFO, "Irrlicht", "Lolo 2: %d %d %d %d", event.GUIEvent.EventType, EGET_BUTTON_CLICKED, id , GUI_ID_QUIT_BUTTON);
        switch(event.GUIEvent.EventType)
        {
            case EGET_BUTTON_CLICKED:   //�����˳���
               // __android_log_print(ANDROID_LOG_INFO, "Irrlicht", "Lolo 3");
                switch(id)
                {
                    case GUI_ID_QUIT_BUTTON:
                       // __android_log_print(ANDROID_LOG_INFO, "Irrlicht", "Lolo 4");
                        gAppAlive = 0;  //ɱ��Ӧ��
                        return true;

                    default:
                        break;
                }
        }
    }
    return false;
}
bool AndroidEventReceiver::IsKeyDown() const
{
	return KeyIsDown;
}

bool AndroidEventReceiver::IsKeyPressed()
{
	if (KeyIsPressed) {
		KeyIsPressed = false;
		return true;
	}
	return false;
}

f32 AndroidEventReceiver::getX() {
	return x;
}
f32 AndroidEventReceiver::getY() {
	return y;
}
u16 AndroidEventReceiver::getDI() {
	return direction;
}

//�������÷���ֵ
void AndroidEventReceiver::setDI(u16 ndi){
	direction = ndi;
}
