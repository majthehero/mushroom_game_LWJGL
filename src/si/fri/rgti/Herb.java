package si.fri.rgti;

public class Herb
    extends GameObject
    implements Interactable {

    /* TODO
     */

    public Herb () {
        // TODO
    }

    @Override
    public void Interact(Player player) {

        // ce player nima polnega lajfa, mu pristejemo 5hp zaradi zelisca
        if(player.HP < player.maxHP) {
            player.HP += 5;
            if(player.HP > player.maxHP) {
                player.HP = player.maxHP;
            }
        }


    }
}
