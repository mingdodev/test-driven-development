package chap02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import study.chap02.PasswordStrength;
import study.chap02.PasswordStrengthMeter;

public class PasswordStrengthMeterTest {

    private PasswordStrengthMeter meter = new PasswordStrengthMeter();

    private void assertStrength(String password, PasswordStrength expStr) {
        PasswordStrength result = meter.meter(password);
        assertEquals(expStr, result);
    }

    @Test
    void 암호_입력값이_없으면_유효하지_않은_암호임을_반환한다() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @Test
    void 암호_입력값이_빈_문자열이면_유효하지_않은_암호임을_반환한다() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @Test
    void 암호가_모든_조건을_충족하면_강도는_강함이다() {
        assertStrength("ab122!AB", PasswordStrength.STRONG);
    }

    @Test
    void 암호_길이가_8글자_미만이고_다른_조건은_충족하면_강도는_보통이다() {
        assertStrength("ab12!AB", PasswordStrength.NORMAL);
    }

    @Test
    void 암호_숫자를_포함하지_않고_다른_조건은_충족하면_강도는_보통이다() {
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }

    @Test
    void 암호_대문자를_포함하지_않고_다른_조건은_충족하면_강도는_보통이다() {
        assertStrength("ab!12@qwerd", PasswordStrength.NORMAL);
    }

    @Test
    void 암호_길이가_8글자_이상인_조건만_충족하면_강도는_약함이다() {
        assertStrength("abcdefgh", PasswordStrength.WEAK);
    }

    @Test
    void 암호_숫자_포함_조건만_충족하면_강도는_약함이다() {
        assertStrength("12345", PasswordStrength.WEAK);
    }

    @Test
    void 암호_대문자_포함_조건만_충족하면_강도는_약함이다() {
        assertStrength("ABCDE", PasswordStrength.WEAK);
    }

    @Test
    void 암호_아무_조건도_충족하지_않으면_강도는_약함이다() {
        assertStrength("abc", PasswordStrength.WEAK);
    }

}
