package com.kenmurrell.zerbert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DataUtilsTest
{
    @Test
    public void SHA1Test1()
    {
        String key = "ThisIsAKey";
        String sha1 = DataUtils.SHA1(key);
        assertEquals("7956f57952cc752bf5e14ca5496d33b25348bb24", sha1);
    }

    @Test
    public void SHA1Test2()
    {
        String key = "test";
        String sha1 = DataUtils.SHA1(key);
        assertEquals("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3", sha1);
    }

    @Test
    public void CryptoTest() throws DataUtils.CryptoException
    {
        String key = "myPassword";
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam tempor lorem sed risus dignissim sodales. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis eu efficitur purus, vitae sagittis orci. Quisque vitae ante egestas, gravida nunc sit amet, pretium ligula. In pharetra eros in eros molestie accumsan. Curabitur tempor pellentesque pulvinar. Maecenas commodo tempor nisl, eu posuere purus pharetra a. Ut pulvinar interdum sem, ullamcorper ultricies erat euismod a. Ut finibus nibh erat, in commodo enim semper eu. Suspendisse nec gravida urna. Duis ornare lacus ut arcu finibus fermentum. Phasellus pellentesque est ut ante faucibus, a facilisis ante sodales. Aliquam porta arcu sed vestibulum interdum. Cras euismod sem ligula, sed accumsan tellus auctor in.\n" +
                "        Nunc venenatis mi venenatis, placerat ex id, luctus purus. Aliquam pulvinar malesuada metus, ut sodales nunc pharetra non. Integer placerat arcu dolor, a imperdiet tellus vestibulum nec. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas quis luctus quam. Ut eu lacinia eros, vitae bibendum libero. Pellentesque laoreet odio ac rutrum maximus. Nunc vel purus sollicitudin, commodo ipsum eu, rutrum arcu. Curabitur eget auctor urna, id elementum enim. Nam rhoncus rhoncus nisl, vel elementum lectus euismod quis.\n" +
                "        Nam nisi leo, malesuada in commodo ac, lacinia sed mi. Donec auctor vulputate convallis. Nam consectetur eros volutpat purus vestibulum, in feugiat libero egestas. Mauris non maximus mauris. Ut ante metus, semper eget tellus finibus, tincidunt tincidunt elit. Fusce ullamcorper, mi et dictum cursus, tellus quam elementum sapien, non euismod nisi orci id nibh. Sed consequat eros in justo hendrerit, nec tincidunt tellus pretium. Nulla nec lectus lacus. Nulla mollis sem vitae dui molestie convallis. Integer tempor orci turpis, ac consectetur ipsum volutpat ut.\n" +
                "        Praesent a erat ut ante tristique elementum. Sed id scelerisque purus. Fusce vel mi sollicitudin sapien egestas lobortis id id tellus. Pellentesque eu efficitur purus, vel dapibus justo. Nullam luctus hendrerit sollicitudin. Integer eu tincidunt turpis. Fusce vitae diam non lorem mollis placerat eget eget ex. Integer ornare lacinia metus, ornare imperdiet tortor aliquam eu.\n" +
                "        Nunc semper mauris nec lectus sagittis gravida. Fusce a mi urna. Nulla imperdiet rutrum purus, quis facilisis mi volutpat non. Vivamus nec eros sapien. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Vestibulum libero quam, venenatis sit amet viverra quis, eleifend at diam. Curabitur vitae diam id leo pharetra tincidunt. Nulla porta eget eros nec vehicula. Mauris eget lacus porttitor, malesuada ex ut, sagittis ipsum. Cras mollis dictum commodo. Quisque eget lacinia felis, id laoreet mauris. Aliquam vel pharetra nunc. Nulla porta risus eu eros semper aliquet. Etiam cursus non ex nec pulvinar. Nullam pellentesque lectus diam, id facilisis magna venenatis a.\n";
        String encrypted = DataUtils.encryptText(text, key);
        String decrypted = DataUtils.decryptText(encrypted, key);
        assertEquals(text, decrypted);
        assertThrows(DataUtils.CryptoException.class, () -> DataUtils.decryptText(encrypted, "notMyPassword"));
        assertNull(null, DataUtils.decryptText(encrypted, ""));
        assertThrows(DataUtils.CryptoException.class, () -> DataUtils.decryptText(encrypted, "PasswordPasswordPassword"));
    }
}
